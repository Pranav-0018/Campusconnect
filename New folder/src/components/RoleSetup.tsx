import { useState } from "react";
import { useMutation } from "convex/react";
import { api } from "../../convex/_generated/api";

export default function RoleSetup() {
  const [role, setRole] = useState<"student" | "teacher">("student");
  const [classValue, setClassValue] = useState("");
  const [section, setSection] = useState("");
  const [bio, setBio] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const createProfile = useMutation(api.profiles.create);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);

    try {
      await createProfile({
        role,
        class: role === "student" ? classValue : undefined,
        section: role === "student" ? section : undefined,
        bio: bio || undefined,
      });
    } catch (error) {
      console.error("Failed to create profile:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen p-4">
      <div className="bg-white rounded-lg shadow-xl p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-purple-600 mb-2">
            Complete Your Profile
          </h1>
          <p className="text-gray-600">Tell us a bit about yourself</p>
        </div>

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              I am a:
            </label>
            <div className="flex space-x-4">
              <label className="flex items-center">
                <input
                  type="radio"
                  value="student"
                  checked={role === "student"}
                  onChange={(e) => setRole(e.target.value as "student")}
                  className="mr-2"
                />
                👨‍🎓 Student
              </label>
              <label className="flex items-center">
                <input
                  type="radio"
                  value="teacher"
                  checked={role === "teacher"}
                  onChange={(e) => setRole(e.target.value as "teacher")}
                  className="mr-2"
                />
                👨‍🏫 Teacher
              </label>
            </div>
          </div>

          {role === "student" && (
            <>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Class
                </label>
                <input
                  type="text"
                  value={classValue}
                  onChange={(e) => setClassValue(e.target.value)}
                  placeholder="e.g., Computer Science"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Section
                </label>
                <input
                  type="text"
                  value={section}
                  onChange={(e) => setSection(e.target.value)}
                  placeholder="e.g., A"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
                />
              </div>
            </>
          )}

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              Bio (Optional)
            </label>
            <textarea
              value={bio}
              onChange={(e) => setBio(e.target.value)}
              placeholder="Tell us about yourself..."
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-purple-500"
            />
          </div>

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-purple-600 text-white py-2 px-4 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 disabled:opacity-50"
          >
            {isSubmitting ? "Setting up..." : "Complete Setup"}
          </button>
        </form>
      </div>
    </div>
  );
}
